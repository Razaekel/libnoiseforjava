/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright � 2010 Thomas J. Hodge (java port of libnoise)
 * Copyright (c) 2014 Nick Whitney (changed perlin basis function,
 * added javadoc, greatly increased the speed)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 */

package libnoiseforjava.module;

import java.util.Random;

import libnoiseforjava.NoiseQuality;
import libnoiseforjava.PerlinBasis;

/** 
 * Noise module that outputs 3-dimensional Perlin noise.
 * <p>
 * <img src="http://libnoise.sourceforge.net/docs/moduleperlin.png">
 * <p>
 * Perlin noise is the sum of several coherent-noise functions of
 * ever-increasing frequencies and ever-decreasing amplitudes.
 * <p>
 * An important property of Perlin noise is that a small change in the
 * input value will produce a small change in the output value, while a
 * large change in the input value will produce a random change in the
 * output value.
 * <p>
 * This noise module outputs Perlin-noise values that usually range from
 * -1.0 to +1.0, but there are no guarantees that all output values will
 * exist within that range.
 * <p>
 * For a better description of Perlin noise, see the links in the
 * <i>References and Acknowledgments</i> section.
 * <p>
 * This noise module does not require any source modules.
 * <p>
 * <b>Octaves</b>
 * <p>
 * The number of octaves control the <i>amount of detail</i> of the
 * Perlin noise.  Adding more octaves increases the detail of the Perlin
 * noise, but with the drawback of increasing the calculation time.
 * <p>
 * An octave is one of the coherent-noise functions in a series of
 * coherent-noise functions that are added together to form Perlin
 * noise.
 * <p>
 * An application may specify the frequency of the first octave by
 * calling the setFrequency() method.
 * <p>
 * An application may specify the number of octaves that generate Perlin
 * noise by calling the setOctaveCount() method.
 * <p>
 * These coherent-noise functions are called octaves because each octave
 * has, by default, double the frequency of the previous octave.  Musical
 * tones have this property as well; a musical C tone that is one octave
 * higher than the previous C tone has double its frequency.
 * <p>
 * <b>Frequency</b>
 * <p>
 * An application may specify the frequency of the first octave by
 * calling the setFrequency() method.
 * <p>
 * <b>Persistence</b>
 * <p>
 * The persistence value controls the <i>roughness</i> of the Perlin
 * noise.  Larger values produce rougher noise.
 * <p>
 * The persistence value determines how quickly the amplitudes diminish
 * for successive octaves.  The amplitude of the first octave is 1.0.
 * The amplitude of each subsequent octave is equal to the product of the
 * previous octave's amplitude and the persistence value.  So a
 * persistence value of 0.5 sets the amplitude of the first octave to
 * 1.0; the second, 0.5; the third, 0.25; etc.
 * <p>
 * An application may specify the persistence value by calling the
 * setPersistence() method.
 * <p>
 * <b>Lacunarity</b>
 * <p>
 * The lacunarity specifies the frequency multipler between successive
 * octaves.
 * <p>
 * The effect of modifying the lacunarity is subtle; you may need to play
 * with the lacunarity value to determine the effects.  For best results,
 * set the lacunarity to a number between 1.5 and 3.5.
 * <p>
 * <b>References &amp; acknowledgments</b>
 * <p>
 * <a href=http://www.noisemachine.com/talk1/>The Noise Machine</a> -
 * From the master, Ken Perlin himself.  This page contains a
 * presentation that describes Perlin noise and some of its variants.
 * He won an Oscar for creating the Perlin noise algorithm!
 * <p>
 * <a
 * href=http://freespace.virgin.net/hugo.elias/models/m_perlin.htm>
 * Perlin Noise</a> - Hugo Elias's webpage contains a very good
 * description of Perlin noise and describes its many applications.  This
 * page gave me the inspiration to create libnoise in the first place.
 * Now that I know how to generate Perlin noise, I will never again use
 * cheesy subdivision algorithms to create terrain (unless I absolutely
 * need the speed.)
 * <p>
 * <a
 * href=http://www.robo-murito.net/code/perlin-noise-math-faq.html>The
 * Perlin noise math FAQ</a> - A good page that describes Perlin noise in
 * plain English with only a minor amount of math.  During development of
 * libnoise, I noticed that my coherent-noise function generated terrain
 * with some "regularity" to the terrain features.  This page describes a
 * better coherent-noise function called <i>gradient noise</i>.  This
 * version of the Perlin module uses gradient coherent noise to
 * generate Perlin noise.
 * 
 * @see <a href="http://libnoise.sourceforge.net/docs/classnoise_1_1module_1_1Perlin.html">noise::module:Perlin</a>
 */
public class Perlin extends ModuleBase
{
	/** 
	 * Default frequency for the noise::module::Perlin noise module.
	 */
	static final double DEFAULT_PERLIN_FREQUENCY = 1.0;

	/** 
	 * Default lacunarity for the noise::module::Perlin noise module.
	 */
	static final double DEFAULT_PERLIN_LACUNARITY = 2.0;

	/** 
	 * Default number of octaves for the noise::module::Perlin noise module.
	 */
	static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;

	/** 
	 * Default persistence value for the noise::module::Perlin noise module.
	 */
	static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;

	/** 
	 * Default noise seed for the noise::module::Perlin noise module.
	 */
	static final int DEFAULT_PERLIN_SEED = 0;

	/** 
	 * Maximum number of octaves for the noise::module::Perlin noise module.
	 */
	static final int PERLIN_MAX_OCTAVE = 30;

	/** 
	 * Frequency of the first octave.
	 */
	double frequency;

	/** 
	 * Frequency multiplier between successive octaves.
	 */
	double lacunarity;
	
	/** 
	 * Total number of octaves that generate the Perlin noise.
	 */
	int octaveCount;

	/** 
	 * Persistence of the Perlin noise.
	 */
	double persistence;

	/** 
	 * Seed value used by the Perlin-noise function.
	 */
	int seed;
	
	private PerlinBasis[] source;
	double[] frequencies;
	double[] amplitudes;

	public Perlin ()
	{
		super(0);
		frequency = DEFAULT_PERLIN_FREQUENCY;
		lacunarity = DEFAULT_PERLIN_LACUNARITY;
		octaveCount = DEFAULT_PERLIN_OCTAVE_COUNT;
		persistence = DEFAULT_PERLIN_PERSISTENCE;
		seed = DEFAULT_PERLIN_SEED;
	}
	
	public void build()
	{
		source = new PerlinBasis[octaveCount];
		frequencies = new double[octaveCount];
		amplitudes = new double[octaveCount];

		Random rnd = new Random(seed);
		
		for(int i = 0; i < octaveCount; i++)
		{
			source[i] = new PerlinBasis();
			
			if(seed != 0)
			{
				seed = rnd.nextInt();
				source[i].setSeed(seed);
			}
			else
			{
				source[i].setSeed(seed);
			}
			
			source[i].setNoiseQuality(noiseQuality);

			frequencies[i] = Math.pow(lacunarity, i);
			amplitudes[i] = Math.pow(persistence, source.length - i);
		}
	}

	@Override
	public double getValue(double x, double y, double z)
	{
		double value = 0;
		double signal = 0;

		for(int i = 0; i < source.length; i++)
		{
			signal = source[i].getValue(x / frequencies[i], y / frequencies[i], z / frequencies[i]);
			value += signal * amplitudes[i];
		}

		return value;
	}

	/** 
	 * Returns the frequency of the first octave.
	 *
	 * @returns The frequency of the first octave.
	 */
	public double getFrequency ()
	{
		return frequency;
	}

	/** 
	 * Returns the lacunarity of the Perlin noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive
	 * octaves.
	 *
	 * @returns The lacunarity of the Perlin noise.
	 */
	public double getLacunarity ()
	{
		return lacunarity;
	}

	/** 
	 * Returns the number of octaves that generate the Perlin noise.
	 * <p>
	 * The number of octaves controls the amount of detail in the Perlin
	 * noise.
	 *
	 * @returns The number of octaves that generate the Perlin noise.
	 */
	public int getOctaveCount ()
	{
		return octaveCount;
	}

	/**
	 * Returns the persistence value of the Perlin noise.
	 * <p>
	 * The persistence value controls the roughness of the Perlin noise.
	 *
	 * @returns The persistence value of the Perlin noise.
	 */
	public double getPersistence ()
	{
		return persistence;
	}

	/** 
	 * Returns the seed value used by the Perlin-noise function.
	 *
	 * @returns The seed value.
	 */
	public int getSeed ()
	{
		return seed;
	}

	/** Sets the frequency of the first octave.
	*
	* @param frequency The frequency of the first octave.
	*/
	public void setFrequency (double frequency)
	{
		this.frequency = frequency;
	}

	/** 
	 * Sets the lacunarity of the Perlin noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive
	 * octaves.
	 * <p>
	 * For best results, set the lacunarity to a number between 1.5 and
	 * 3.5 (~2 is best).
	 * <p>
	 * @param lacunarity The lacunarity of the Perlin noise.
	 */
	public void setLacunarity (double lacunarity)
	{
		this.lacunarity = lacunarity;
	}

	/** 
	 * Sets the number of octaves that generate the Perlin noise.
	 * <p>
	 * The number of octaves controls the amount of detail in the Perlin
	 * noise.
	 * <p>
	 * The larger the number of octaves, the more time required to
	 * calculate the Perlin-noise value.
	 *
	 * @param octaveCount The number of octaves that generate the Perlin
	 * noise.
	 *
	 * @pre The number of octaves ranges from 1 to PERLIN_MAX_OCTAVE.
	 */
	public void setOctaveCount (int octaveCount)
	{
		if (octaveCount < 1)
		{
			octaveCount = 1;
		}
		else if(octaveCount > PERLIN_MAX_OCTAVE)
		{
			octaveCount = PERLIN_MAX_OCTAVE;
		}

		this.octaveCount = octaveCount;
	}

	/**
	 * Sets the persistence value of the Perlin noise.
	 * <p>
	 * The persistence value controls the roughness of the Perlin noise.
	 * <p>
	 * For best results, set the persistence to a number between 0.0 and
	 * 1.0.
	 * 
	 * @param persistence The persistence value of the Perlin noise.
	 */
	public void setPersistence (double persistence)
	{
		this.persistence = persistence;
	}

	/** 
	 * Sets the seed value used by the Perlin-noise function.
	 *
	 * @param seed The seed value.
	 */
	public void setSeed (int seed)
	{
		this.seed = seed;
	}

}
